package com.mintos.qa_live_test_app.repository;

import com.mintos.qa_live_test_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> getUserByPersonId(Integer personId);
}
