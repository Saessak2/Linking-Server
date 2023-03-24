package com.linking.document;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("G")
@PrimaryKeyJoinColumn(name = "group_id")
@Table(name = "\"group\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends Document{

    /**
     * field
     */

    /**
     * constructor
     */

    @Builder
    public Group(String name, int doc_depth, int doc_index, Document parent, List<Document> documentList) {
        super(name, doc_depth, doc_index, parent, documentList);
    }

    /**
     * method
     */


}

