package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Introspected // <1>
public class CommandBookSave {
    @NotBlank // <2>
    private String title;

    @NotNull // <3>
    @Positive // <4>
    private Integer pages;

    public CommandBookSave() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
