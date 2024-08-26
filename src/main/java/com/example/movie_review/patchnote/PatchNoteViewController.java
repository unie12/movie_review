package com.example.movie_review.patchnote;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/patch-notes")
public class PatchNoteViewController {
    private final PatchNoteService patchNoteService;

    @GetMapping
    public String listPatchNotes(Model model) throws IOException {
        List<PatchNote> patchNotes = patchNoteService.getAllPatchNotes();
        model.addAttribute("patchNotes", patchNotes);
        return "patch-notes/list";
    }

    @GetMapping("/{version}")
    public String viewPatchNote(@PathVariable String version, Model model) throws IOException {
        PatchNote patchNote = patchNoteService.getPatchNoteByVersion(version);
        model.addAttribute("patchNote", patchNote);
        return "patch-notes/view";
    }

//    @GetMapping("/{id}")
//    public String viewPatchNote(@PathVariable Long id, Model model) {
//        PatchNote patchNote = patchNoteRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Path note not found"));
//        model.addAttribute("patchNote", patchNote);
//        return "patch-notes/view";
//    }
}
