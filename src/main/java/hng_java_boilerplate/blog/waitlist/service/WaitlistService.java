package hng_java_boilerplate.blog.waitlist.service;

import hng_java_boilerplate.blog.waitlist.repository.WaitlistRepository;
import hng_java_boilerplate.blog.waitlist.entity.Waitlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WaitlistService {

    private final WaitlistRepository waitlistRepository;

    public WaitlistService(WaitlistRepository waitlistRepository) {
        this.waitlistRepository = waitlistRepository;
    }

    public Waitlist saveWaitlist(Waitlist waitlist) {
        return waitlistRepository.save(waitlist);
    }

    public Page<Waitlist> getWaitlistUsers(Pageable pageable) {
        return waitlistRepository.findAll(pageable);
    }
}
