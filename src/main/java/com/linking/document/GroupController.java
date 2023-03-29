package com.linking.document;

import com.linking.document.dto.GroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

//    @RequestMapping(method = RequestMethod.POST, path = "/") 과 같음
    @PostMapping()
    public String requestCreateGroup(@RequestBody GroupDto groupDto) {
        groupService.createNewGroup(groupDto);
        return "OK";
    }

}
