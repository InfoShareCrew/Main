package com.infoShare.calog.domain.Notice;

import com.infoShare.calog.domain.Cafe.Cafe;
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
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Page<Notice> getList(int page, Long cafeId) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.noticeRepository.findByCafeId(pageable, cafeId);
    }

    public Page<Notice> searchNotices(String keyword, int page, Long cafeId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")));
        return noticeRepository.findByTitleContainingOrContentContainingAndCafeId(keyword, keyword, cafeId, pageable);
    }

    public Notice getNoticeById(Long id) {
        Optional<Notice> notice  = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            if (notice.get().getAuthor() == null) {
                throw new DataNotFoundException("Author not found for notice");
            }
            return notice.get();
        } else {
            throw new DataNotFoundException("Notice not found");
        }
    }


    public void create(String title, String content, SiteUser author, Cafe cafe) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setAuthor(author);
        notice.setCafe(cafe);
        this.noticeRepository.save(notice);
    }

    public void viewUp(Notice notice) {
        notice.setView(notice.getView() + 1);
        this.noticeRepository.save(notice);
    }

    public Optional<Notice> findById(Long id) {
        return this.noticeRepository.findById(id);
    }


    public void modify(Notice notice, String title, String content) {
        notice.setTitle(title);
        notice.setContent(content);
        this.noticeRepository.save(notice);
    }

    public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }

    public Page<Notice> searchNotices(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")));
        return noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }
}
