package com.example.movie_review.patchnote;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatchNoteService {
    private static final String PATCH_NOTES_DIR = "classpath:patch-notes/";

    private final ResourceLoader resourceLoader;
    private final Parser parser = com.vladsch.flexmark.parser.Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    @Cacheable("patchNotes")
    public List<PatchNote> getAllPatchNotes() throws IOException {
        Resource resource = resourceLoader.getResource(PATCH_NOTES_DIR);
        File dir = resource.getFile();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".md"));

        List<PatchNote> patchNotes = new ArrayList<>();
        for (File file : files) {
            PatchNote patchNote = parsePathNoteFile(file);
            patchNotes.add(patchNote);
        }

        patchNotes.sort(Comparator.comparing(PatchNote::getReleaseDate).reversed());
        return patchNotes;
    }

    @Cacheable("patchNote")
    public PatchNote getPatchNoteByVersion(String version) throws IOException {
        List<PatchNote> allNotes = getAllPatchNotes();
        return allNotes.stream()
                .filter(note -> note.getVersion().equals(version))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Patch note not found for version: " + version));
    }


    /**
     *  file 내용 읽고 파싱해서 patchNote 객체 생성
     */
    private PatchNote parsePathNoteFile(File file) throws IOException{
        String content = new String(Files.readAllBytes(file.toPath()));
        String[] parts = content.split("---", 3);
        Map<String, String> metadata = parseMetadata(parts[1]);

        String htmlContent = renderer.render(parser.parse(parts[2].trim()));
        return new PatchNote(
                metadata.get("version"),
                LocalDate.parse(metadata.get("date")),
                LocalDate.parse(metadata.getOrDefault("modify", metadata.get("date"))),
                metadata.get("title"),
                htmlContent
        );
    }

    @Scheduled(cron = "0 0 9 * * MON")
    @CacheEvict(cacheNames = {"patchNotes", "patchNote"}, allEntries = true)
    public void refreshPatchNotes() {
        try {
            getAllPatchNotes();
            log.info("패치노트 캐시가 성공적으로 갱신되었습니다.");
        } catch (IOException e) {
            log.error("패치노트 갱신 중 오류 발생", e);
            throw new RuntimeException("패치노트 갱신 실패", e);
        }
    }

    private Map<String, String> parseMetadata(String metadataContent) {
        Map<String, String> metadata = new HashMap<>();
        String[] lines = metadataContent.trim().split("\n");
        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if(parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                metadata.put(key, value);
            }
        }
        return metadata;
    }

}
