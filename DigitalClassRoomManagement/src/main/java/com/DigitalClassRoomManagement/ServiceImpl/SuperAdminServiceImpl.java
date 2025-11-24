package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Enum.Status;
import com.DigitalClassRoomManagement.Repository.UserRepository;
import com.DigitalClassRoomManagement.Service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminServiceImpl implements SuperAdminService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getunApprovedStatusRequest( )
    {
        try
        {
            List<User> ad = userRepository.findAll();
            List<User> unapprovedAdmins = ad.stream()
                    .filter(a -> a.getStatus() == Status.UNAPPROVED  && a.getRole()== Role.ADMIN)
                    .toList();
          return unapprovedAdmins;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public List<User> getApprovedStatusRequest( )
    {
        try
        {
            List<User> ad = userRepository.findAll();
            List<User> approvedAdmins = ad.stream()
                    .filter(a -> a.getStatus() == Status.APPROVED  && a.getRole()== Role.ADMIN)
                    .toList();
            return approvedAdmins;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    @Override
    public User updateStatus(Long id, Status status)
    {
        try
        {
            Optional<User> u=userRepository.findById(id);
            if(u.isPresent())
            {
                User u1=u.get();
                if(u1.getStatus()==status)
                {
                    throw new RuntimeException("Already Done");
                }else {
                    u1.setStatus(status);
                    return userRepository.save(u1);
                }
            }
            throw new RuntimeException("UserNotFoud");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
