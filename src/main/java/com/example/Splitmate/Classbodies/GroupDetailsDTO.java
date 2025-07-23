package com.example.Splitmate.Classbodies;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class GroupDetailsDTO {
    private Long groupId;
    private String groupName;
    private String dateOfCreation;
    private List<String> adminList;

    // default constructor

    public GroupDetailsDTO(Long groupId, String groupName, String dateOfCreation) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.dateOfCreation = dateOfCreation;
    }
}
