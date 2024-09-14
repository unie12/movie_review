package com.example.movie_review.patchnote;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatchNoteService {
    private static final String PATCH_NOTES_DIR = "patch-notes/";

    private final ResourceLoader resourceLoader;
    private final ResourcePatternResolver resourcePatternResolver;
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    @Cacheable("patchNotes")
    public List<PatchNote> getAllPatchNotes() throws IOException {
        Resource[] resources = resourcePatternResolver.getResources("classpath*:" + PATCH_NOTES_DIR + "*.md");

        List<PatchNote> patchNotes = new ArrayList<>();
        for (Resource resource : resources) {
            PatchNote patchNote = parsePathNoteResource(resource);
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

    private PatchNote parsePathNoteResource(Resource resource) throws IOException {
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String[] parts = content.split("---", 3);
        Map<String, String> metadata = parseMetadata(parts[1]);

        List<PatchNoteSection> sections = parseSections(parts[2].trim());
        return new PatchNote(
                metadata.get("version"),
                LocalDate.parse(metadata.get("date")),
                LocalDate.parse(metadata.getOrDefault("modify", metadata.get("date"))),
                metadata.get("title"),
                metadata.get("thumbnail"),
                sections
        );
    }


    /**
     *  file 내용 읽고 파싱해서 patchNote 객체 생성
     */
    private PatchNote parsePathNoteFile(File file) throws IOException{
        String content = new String(Files.readAllBytes(file.toPath()));
        String[] parts = content.split("---", 3);
        Map<String, String> metadata = parseMetadata(parts[1]);

        List<PatchNoteSection> sections = parseSections(parts[2].trim());
        return new PatchNote(
                metadata.get("version"),
                LocalDate.parse(metadata.get("date")),
                LocalDate.parse(metadata.getOrDefault("modify", metadata.get("date"))),
                metadata.get("title"),
                metadata.get("thumbnail"),
                sections
        );
    }

    @Scheduled(cron = "0 0 9 * * MON", zone = "Asia/Seoul")
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

    private List<PatchNoteSection> parseSections(String content) {
        List<PatchNoteSection> sections = new ArrayList<>();
        String[] sectionStrings = content.split("# ");
        for (String section : sectionStrings) {
            if (!section.trim().isEmpty()) {
                String[] lines = section.trim().split("\n", 2);
                String title = lines[0].trim();
                String sectionContent = lines[1].trim();

                String imageUrl = null;
                if (sectionContent.startsWith("![")) {
                    String[] contentParts = sectionContent.split("\n", 2);
                    imageUrl = extractImageUrl(contentParts[0]);
                    sectionContent = contentParts[1].trim();
                }

                sections.add(new PatchNoteSection(title, imageUrl, renderer.render(parser.parse(sectionContent))));
            }
        }
        return sections;
    }

    private String extractImageUrl(String imageMd) {
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(imageMd);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
