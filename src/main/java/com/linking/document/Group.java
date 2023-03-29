package com.linking.document;

import com.linking.project.Project;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("G")
@PrimaryKeyJoinColumn(name = "group_id")
@Table(name = "\"group\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends Document{


    /**
     * constructor
     */

    @Builder
    public Group(String name, int doc_depth, int doc_index, Document parent, List<Document> childDocList, Project project) {
        super(name, doc_depth, doc_index, parent, childDocList, project);
    }



    /**
     * method
     */


}

