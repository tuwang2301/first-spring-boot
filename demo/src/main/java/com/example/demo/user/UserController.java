package com.example.demo.user;

import com.example.demo.common.ResponseObject;
import com.example.demo.pagination.PaginatedResponse;
import com.example.demo.pagination.PaginationMeta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @Operation(summary = "Lấy ra tất cả account")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "3") Integer pageSize
    ) {
        try {
            Page<UserProjection> userPage = userService.getAllUsers(username, currentPage, pageSize);
            PaginationMeta meta = new PaginationMeta(
                    currentPage,
                    pageSize,
                    (int) userPage.getTotalElements(),
                    userPage.getTotalPages()
            );
            return ResponseEntity.ok(new ResponseObject<>("success", "Search successfully", new PaginatedResponse(meta,userPage.getContent())));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail","Search fail",e.getMessage()));
        }
    }

    @PostMapping("/add-user")
    @Operation(summary = "Thêm mới tài khoản")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> addNewUser(@RequestBody ApplicationUserDTO newUser){
        try{
            userService.addNewUser(newUser);
            return ResponseEntity.ok(new ResponseObject<>("success","Add successfully",newUser));
        }catch(UserException u){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail","Add fail",u.getUserErrors().getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail","Add fail",e.getMessage()));
        }
    }

}
