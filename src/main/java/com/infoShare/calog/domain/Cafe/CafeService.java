package com.infoShare.calog.domain.Cafe;

import java.util.*;

import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final UserService userService;

    public Page<Cafe> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.cafeRepository.findAll(pageable);
    }

    public List<Cafe> getMyList(String userEmail) { // 내가 가입한 카페 리스트
        SiteUser user = userService.getUser(userEmail);
        if (user == null) {
            throw new DataNotFoundException("User not found with email: " + userEmail);
        }
        Set<Cafe> cafeSet = user.getCafe();
        return new ArrayList<>(cafeSet);
    }

    public List<Cafe> getOwnList(String userEmail) {        // 내가 만든 카페 리스트
        return cafeRepository.findAllByManager(userService.getUser(userEmail));
    }

    public Cafe getCafeById(Long id) {
        Optional<Cafe> cafe = this.cafeRepository.findById(id);
        if (cafe.isPresent()) {
            return cafe.get();
        } else {
            throw new DataNotFoundException("Cafe not found");
        }
    }

    public Cafe findById(Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cafe ID"));
    }

    public Cafe create(String name, String intro ,SiteUser manager) {
        Cafe cafe = new Cafe();
        cafe.setName(name);
        cafe.setIntro(intro);
        cafe.setManager(manager);
        return cafeRepository.save(cafe);
    }

    public void modifyCafe(Cafe cafe, String name, String intro, String profileImg) {
        cafe.setName(name);
        cafe.setIntro(intro);
        cafe.setProfileImg(profileImg);
        this.cafeRepository.save(cafe);
    }

    public void deleteCafe(Cafe cafe) {
        this.cafeRepository.delete(cafe);
    }

    public void vote(Cafe cafe, SiteUser siteUser) {
        cafe.getVoter().add(siteUser);
        this.cafeRepository.save(cafe);
    }

    public void signup(Cafe cafe, SiteUser siteUser) {
        siteUser.getCafe().add(cafe);
        this.userService.save(siteUser);
    }
}
