package example.micronaut;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WebController.class)
@TestPropertySource(properties = "logging.level.org.springframework.web=DEBUG")
public class HandlingFormSubmissionApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void rendersForm() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(content().string(containsString("Name:")));
    }

    @Test
    public void submitsForm() throws Exception {
        mockMvc.perform(post("/").param("name", "N").param("age", "15"))
                .andExpect(content().string(containsString("name=\"name\" value=\"N\"")))
                .andExpect(content().string(containsString("name=\"age\" value=\"15\"")))
                .andExpect(content().string(containsString("size must be between 2 and 30")))
                .andExpect(content().string(containsString("must be greater than or equal to 18")));

        mockMvc.perform(post("/").param("name", "N"))
                .andExpect(content().string(containsString("name=\"name\" value=\"N\"")))
                .andExpect(content().string(containsString("name=\"age\" value=\"\"")))
                .andExpect(content().string(containsString("size must be between 2 and 30")))
                .andExpect(content().string(containsString("must not be null")));
    }

}