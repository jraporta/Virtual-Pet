package cat.jraporta.virtualpet.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class Pet<ID> {

    private ID id;
    private String name;
    private ID userId;

}
