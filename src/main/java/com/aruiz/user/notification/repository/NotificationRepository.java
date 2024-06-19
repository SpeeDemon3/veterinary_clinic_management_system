package com.aruiz.user.notification.repository;

import com.aruiz.user.notification.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
}
