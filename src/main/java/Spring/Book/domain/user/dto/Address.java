package Spring.Book.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Address {

    private String zipcode;
    private String address;
    private String detailAddr;
    private String subAddr;

    @Builder
    public Address(String zipcode, String address, String detailAddr, String subAddr) {
        this.zipcode = zipcode;
        this.address = address;
        this.detailAddr = detailAddr;
        this.subAddr = subAddr;
    }
}
