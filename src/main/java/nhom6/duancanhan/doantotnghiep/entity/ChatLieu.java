package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_lieu")
public class ChatLieu extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Tên chất liệu không được để trống!")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Tên chất liệu không được chứa ký tự đặc biệt!")
    @Pattern(regexp = "^[^\\s].*[^\\s]$", message = "Tên chất liệu không được chứa khoảng trắng ở đầu hoặc cuối!")
    @Size(max = 19, message = "Name must be 20 characters")
    @Column(name = "ten_chat_lieu",unique = true)
    private String tenChatLieu;

    @Column(name = "trang_thai")
    private Integer trangThai;
}