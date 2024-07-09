package com.example.ifclubserver.member.presentation;

import com.example.ifclubserver.member.application.MemberService;
import com.example.ifclubserver.member.domain.dto.MemberDto;
import com.example.ifclubserver.member.domain.dto.request.LoginMemberRequest;
import com.example.ifclubserver.member.domain.dto.request.SignUpMemberRequest;
import com.example.ifclubserver.member.domain.dto.response.LoginMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponse> login(
            @RequestBody LoginMemberRequest request
    ) {
        LoginMemberResponse memberLoginResponse = memberService.login(request);
        return ResponseEntity.ok()
                .header("Authorization",
                        "Bearer " + memberLoginResponse.accessToken()).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(
            @RequestBody SignUpMemberRequest request
    ) {
        MemberDto savedMember = memberService.save(request);
        return ResponseEntity.ok(savedMember);
    }

}
