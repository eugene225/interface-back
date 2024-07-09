package com.example.ifclubserver.member.presentation;

import com.example.ifclubserver.common.docs.MemberControllerDocs;
import com.example.ifclubserver.member.application.impl.MemberServiceImpl;
import com.example.ifclubserver.member.domain.dto.MemberDto;
import com.example.ifclubserver.member.domain.dto.request.CreateMemberRequest;
import com.example.ifclubserver.member.domain.dto.request.UpdateMemberRequest;
import com.example.ifclubserver.member.domain.dto.response.CreateMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberControllerDocs {

    private final MemberServiceImpl memberServiceImpl;

    @PostMapping("/")
    public ResponseEntity<CreateMemberResponse> create(@RequestBody CreateMemberRequest form) {
        // 멤버 저장 로직 호출
        CreateMemberResponse createMemberResponse = memberServiceImpl.createMember(form);

        return ResponseEntity.ok(createMemberResponse);
    }

    @GetMapping("/")
    public ResponseEntity<List<MemberDto>> getMembers() {
        List<MemberDto> memberDtos = memberServiceImpl.getMembers();
        return ResponseEntity.ok(memberDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable("id") Long id) {
        MemberDto memberDto = memberServiceImpl.getMember(id);
        return ResponseEntity.ok(memberDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberServiceImpl.deleteMember(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id,
                                                  @RequestBody UpdateMemberRequest memberUpdate) {
        return ResponseEntity.ok().body(memberServiceImpl.updateMember(id, memberUpdate));
    }

}
