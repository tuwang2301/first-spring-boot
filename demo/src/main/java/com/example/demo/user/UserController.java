package com.example.demo.user;

import com.example.demo.common.ResponseObject;
import com.example.demo.pagination.PaginatedResponse;
import com.example.demo.pagination.PaginationMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
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

}
