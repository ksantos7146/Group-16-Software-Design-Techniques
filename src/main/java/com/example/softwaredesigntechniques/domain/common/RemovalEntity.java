package com.example.softwaredesigntechniques.domain.common;

import com.example.softwaredesigntechniques.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class RemovalEntity extends AuditedEntity {

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "deleted_by")
  private User deletedBy;
} 