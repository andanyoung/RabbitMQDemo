package cn.andyoung.rabbitmq.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {
  private static final long serialVersionUID = 1584658459421L;
  private String username;
  private String pwd;
}
