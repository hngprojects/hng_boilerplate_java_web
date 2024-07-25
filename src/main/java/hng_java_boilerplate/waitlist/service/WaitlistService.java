package hng_java_boilerplate.waitlist.service;

import hng_java_boilerplate.waitlist.entity.Waitlist;
import hng_java_boilerplate.waitlist.repository.WaitlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
