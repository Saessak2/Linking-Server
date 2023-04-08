package com.linking.pageCheck.dto;

import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserTempRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageCheckRes {

    private Long pageCheckId;
    private Long pageId;
    private String lastChecked;
    private Long userId;
    private String userName;
//    private UserTempRes userDetailedRes;

//    public String getUserName() {
//        return userDetailedRes.getLastName() + userDetailedRes.getFirstName();
//    }

}
