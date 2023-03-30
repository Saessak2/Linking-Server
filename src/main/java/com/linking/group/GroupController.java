package com.linking.group;

import com.linking.group.dto.GroupReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;


//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public void createGroup(@RequestBody GroupReqDto groupReqDto) {
//    }
}
