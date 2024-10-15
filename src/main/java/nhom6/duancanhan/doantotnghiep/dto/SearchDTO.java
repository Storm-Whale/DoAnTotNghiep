package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    @NotBlank(message = "Not null")
    @Size(min = 3, max = 20, message = "keyword từ 3 kí tự trở lên và nhỏ hơn 20 kí tự")
    private String keyword;
}
