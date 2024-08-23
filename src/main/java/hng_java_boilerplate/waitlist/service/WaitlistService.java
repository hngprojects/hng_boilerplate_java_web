package hng_java_boilerplate.waitlist.service;

import hng_java_boilerplate.waitlist.dto.WaitlistRequestDto;
import hng_java_boilerplate.waitlist.entity.Waitlist;
import hng_java_boilerplate.waitlist.repository.WaitlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WaitlistService {

    private final WaitlistRepository waitlistRepository;

    public WaitlistService(WaitlistRepository waitlistRepository) {
        this.waitlistRepository = waitlistRepository;
    }

    public Waitlist createAndSaveWaitlist(WaitlistRequestDto waitlistRequestDTO) {
        Waitlist waitlist = new Waitlist();
        waitlist.setFullName(waitlistRequestDTO.getFullName());
        waitlist.setEmail(waitlistRequestDTO.getEmail());
        return waitlistRepository.save(waitlist);
    }

    public Page<Waitlist> getWaitlistUsers(Pageable pageable) {
        return waitlistRepository.findAll(pageable);
    }
}
