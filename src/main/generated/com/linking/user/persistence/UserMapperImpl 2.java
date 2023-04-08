package com.linking.user.persistence;

import com.linking.user.domain.User;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserSignUpDefaultReq;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

import com.linking.user.dto.UserSignUpReq;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-04T21:03:23+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDetailedRes toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDetailedRes.UserDetailedResBuilder userDetailedRes = UserDetailedRes.builder();

        userDetailedRes.userId( user.getUserId() );
        userDetailedRes.lastName( user.getLastName() );
        userDetailedRes.firstName( user.getFirstName() );
        userDetailedRes.email( user.getEmail() );
        userDetailedRes.phoneNumber( user.getPhoneNumber() );
        userDetailedRes.password( user.getPassword() );

        return userDetailedRes.build();
    }

    @Override
    public List<UserDetailedRes> toDto(List<User> list) {
        if ( list == null ) {
            return null;
        }

        List<UserDetailedRes> list1 = new ArrayList<UserDetailedRes>( list.size() );
        for ( User user : list ) {
            list1.add( toDto( user ) );
        }

        return list1;
    }

    @Override
    public User toEntity(UserSignUpDefaultReq userSignUpDefaultReq) {
        if ( userSignUpDefaultReq == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.lastName( userSignUpDefaultReq.getLastName() );
        user.firstName( userSignUpDefaultReq.getFirstName() );
        user.email( userSignUpDefaultReq.getEmail() );
        user.phoneNumber( userSignUpDefaultReq.getPhoneNumber() );
        user.password( userSignUpDefaultReq.getPassword() );

        return user.build();
    }
}
