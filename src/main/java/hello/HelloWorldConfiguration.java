package hello; /**
 * Created with IntelliJ IDEA.
 * User: WEI
 * Date: 6/5/15
 * Time: 5:12 AM
 * To change this template use File | Settings | File Templates.
 */

import org.hibernate.validator.constraints.NotEmpty;

public class HelloWorldConfiguration extends io.dropwizard.Configuration {
    @NotEmpty
    private String template = "Hello";

    @NotEmpty
    private String defaultName = "Stranger";

    //@JsonProperty
    public String getTemplate() {
        return template;
    }

    //@JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    //@JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    //@JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }
}
