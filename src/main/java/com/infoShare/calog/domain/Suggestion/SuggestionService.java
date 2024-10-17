package com.infoShare.calog.domain.Suggestion;

import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;

    public Page<Suggestion> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.suggestionRepository.findAll(pageable);
    }

    public Suggestion getSuggestionById(Long id) {
        Optional<Suggestion> suggestion = this.suggestionRepository.findById(id);
        if (suggestion.isPresent()) {
            return suggestion.get();
        } else {
            throw new DataNotFoundException("Suggestion not found");
        }
    }

    public void createSuggestion(String title, String content) {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle(title);
        suggestion.setContent(content);
        this.suggestionRepository.save(suggestion);
    }

    public void viewUp(Suggestion suggestion) {
        suggestion.setView(suggestion.getView() + 1);
        this.suggestionRepository.save(suggestion);
    }

    public void modifySuggestion(Suggestion suggestion, String title, String content) {
        suggestion.setTitle(title);
        suggestion.setContent(content);
        this.suggestionRepository.save(suggestion);
    }

    public void delete(Suggestion suggestion) {
        this.suggestionRepository.delete(suggestion);
    }

    public void vote(Suggestion suggestion, SiteUser siteUser) {
        suggestion.getVoter().add(siteUser);
        this.suggestionRepository.save(suggestion);
    }
}

