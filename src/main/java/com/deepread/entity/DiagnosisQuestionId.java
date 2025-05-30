package com.deepread.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisQuestionId implements Serializable {
    private String type;
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiagnosisQuestionId)) return false;
        DiagnosisQuestionId that = (DiagnosisQuestionId) o;
        return Objects.equals(type, that.type) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
