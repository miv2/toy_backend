package co.kr.toybackend;

import co.kr.toybackend.member.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final MemberJpaRepository memberJpaRepository;

    public TestService(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }




}
