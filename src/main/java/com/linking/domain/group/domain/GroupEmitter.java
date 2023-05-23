package com.linking.domain.group.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@Setter
@RedisHash("groupEmitter")
public class GroupEmitter {


}

