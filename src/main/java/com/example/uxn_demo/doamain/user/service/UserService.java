package com.example.uxn_demo.doamain.user.service;

import com.example.uxn_demo.doamain.user.domain.User;
import com.example.uxn_demo.doamain.user.domain.UserAuthority;
import com.example.uxn_demo.doamain.user.domain.repository.UserRepository;
import com.example.uxn_demo.doamain.user.web.dto.res.UserInfoResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    @Transactional
    public void addAuthority(Long id, String authority){
        userRepository.findById(id).ifPresent(user -> {
            UserAuthority newRole = new UserAuthority(user.getIdx(), authority);
            if(user.getAuthorities() == null){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
//                User.builder().authorities(user.getAuthorities());
                saveUser(user);
            }else if(!user.getAuthorities().contains(newRole)){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
//                User.builder().authorities(user.getAuthorities());
                saveUser(user);
            }
        });
    }



//    @Transactional
//    public void removeAuthority(Long id, String authority){
//        userRepository.findById(id).ifPresent(user -> {
//            if(user.getAuthorities()==null) return;
//            UserAuthority targetRole = new UserAuthority(user.getIdx(), authority);
//            if(user.getAuthorities().contains(targetRole)){
//                user.setAuthorities(
//                        user.getAuthorities().stream().filter(auth->!auth.equals(targetRole))
//                                .collect(Collectors.toSet())
//                );
//                saveUser(user);
//            }
//        });
//    }

    @Transactional
    public User saveUser(User user){

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByUserIdAndPassword(String id, String password){

        return userRepository.findByUserIdAndPassword(id, password).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public UserInfoResDto findByIdx(Long idx){
        User user = userRepository.findById(idx).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        return new UserInfoResDto(user);

    }

    @Transactional(readOnly = true)
    public UserInfoResDto findByUserId(String id){
        User user = userRepository.findByUserId(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        return new UserInfoResDto(user);
    }

}
