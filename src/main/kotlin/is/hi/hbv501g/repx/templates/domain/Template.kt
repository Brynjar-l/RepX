package `is`.hi.hbv501g.repx.templates.domain

import `is`.hi.hbv501g.repx.users.domain.User
import `is`.hi.hbv501g.repx.templates.domain.TemplateExercise
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(
    name = "templates",
    uniqueConstraints = [
        UniqueConstraint(name = "uq_template_name_per_user", columnNames = ["user_id", "name"])
    ]
)
data class Template(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    val user: User,

    @Column(nullable = false)
    var name: String,

    @Column
    var notes: String? = null,

    @OneToMany(
        mappedBy = "template",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val exercises: MutableList<TemplateExercise> = mutableListOf()
)
