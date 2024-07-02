package com.example.ifclubserver.member.application.impl;

import com.example.ifclubserver.member.application.MemberService;
import com.example.ifclubserver.member.domain.dto.MemberDto;
import com.example.ifclubserver.member.domain.dto.request.CreateMemberRequest;
import com.example.ifclubserver.member.domain.dto.request.UpdateMemberRequest;
import com.example.ifclubserver.member.domain.dto.response.CreateMemberResponse;
import com.example.ifclubserver.member.domain.entity.Member;
import com.example.ifclubserver.member.domain.repository.MemberRepository;
import com.example.ifclubserver.member.exception.MemberErrorType;
import com.example.ifclubserver.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public CreateMemberResponse createMember(CreateMemberRequest form) {

        Member createdMember = form.toEntity();

        return CreateMemberResponse.from(memberRepository.save(createdMember));
    }

    @Override
    public MemberDto getMember(Long id) {
        // DB에서 Member 가져오기
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));

        // Optional인 Member를 Member로 바꾸는거
        return MemberDto.from(member);
    }

    @Override
    public List<MemberDto> getMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
            .map(MemberDto::from)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    @Override
    public MemberDto updateMember(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        member.updateName(request.name());
        member.updateStudentId(request.studentId());
        member.updatePhone(request.phone());
        member.updateEmail(request.email());

        return MemberDto.from(memberRepository.save(member));
    }

    public MemberDto save(MemberSignUpForm form) {
        // 이메일 중복 확인
        if (memberRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new MemberException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        return MemberDto.from(
            memberRepository.save(Member.builder()
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .characterName(form.getCharacterName())
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .profileImageUrl(null)
                .totalScore(0) // 초기 호감도는 0으로 설정
                .birthDate(form.getBirthDate())
                .gender(form.getGender())
                .mbti(form.getMbti())
                .totalRegressionCount(0) // 초기 회귀 횟수는 0으로 설정
                .gameProgress("NOT_STARTED") // 초기 게임 진행 상태는 'NOT_STARTED'로 설정
                .refreshToken(null)
                .build()
            )
        );
    }

    public MemberLoginResponse login(MemberLoginForm form) {
        // 아이디 검증
        Member member = memberRepository.findByEmail(form.getEmail())
            .orElseThrow(() -> new MemberException(ErrorCode.EMAIL_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordEncoder.matches(form.getPassword(), member.getPassword())) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(
            CustomUserDetails.builder()
                .member(member)
                .build());
        String refreshToken = tokenService.createRefreshToken(member);

        return MemberLoginResponse.from(member, accessToken, refreshToken);
    }
}
