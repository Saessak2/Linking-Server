package com.linking.user.service;

import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.service.ProjectService;
import com.linking.user.domain.User;
import com.linking.user.dto.*;
import com.linking.user.persistence.UserRepository;
import com.linking.user.persistence.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final ParticipantRepository participantRepository;
    private final ProjectService projectService;

    public Optional<UserDetailedRes> addUser(UserSignUpReq userSignUpReq)
            throws DataIntegrityViolationException {
        return Optional.of(
                userMapper.toDto(
                        userRepository.save(
                                userMapper.toEntity(userSignUpReq))));
    }

    public boolean isUniqueEmail(UserEmailVerifyReq emailReq){
        return userRepository.findUserByEmail(emailReq.getEmail()).isPresent();
    }

    //TODO 여기가문제라는건가??
    public List<UserDetailedRes> getUsersByPartOfEmail(UserEmailReq userEmailReq)
        throws NoSuchElementException{
        List<User> userList = userRepository.findUsersByPartOfEmail(userEmailReq.getPartOfEmail());
        if (!userList.isEmpty() && userEmailReq.getProjectId() != -1L) {
            List<Participant> possibleParticipants =
                    participantRepository.findByProject(new Project(userEmailReq.getProjectId()));
            if (!possibleParticipants.isEmpty())
                userList = userList.stream().filter(
                        user -> possibleParticipants.stream().noneMatch(
                                        participant -> user.getUserId().equals(participant.getUser().getUserId())))
                        .collect(Collectors.toList());
        }

        if(userList.isEmpty())
            throw new NoSuchElementException();
        return userMapper.toDto(userList);
    }

    public Optional<UserDetailedRes> getUserById(Long userId)
            throws NoSuchElementException{
        return Optional.ofNullable(userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(NoSuchElementException::new));
    }

    public Optional<UserDetailedRes> getUserWithEmailAndPw(UserSignInReq userSignInReq)
            throws NoSuchElementException{
        String email = userSignInReq.getEmail(), pw = userSignInReq.getPassword();
        return Optional.ofNullable(userRepository.findUserByEmailAndPassword(email, pw)
                .map(userMapper::toDto)
                .orElseThrow(NoSuchElementException::new));
    }

    public void deleteUser(Long userId)
            throws EmptyResultDataAccessException, DataIntegrityViolationException {
        userRepository.deleteById(userId);

        // TODO 프젝을 소유하는 소유자에 해당하는 사용자인 경우 소유자 이전해야힘.
    }

}
