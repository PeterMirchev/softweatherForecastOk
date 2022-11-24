package softuni.exam.models.dto;

import softuni.exam.models.entity.enums.DayOfWeek;
import softuni.exam.util.impl.TimeAdapter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Time;
import java.time.LocalTime;

@XmlRootElement(name = "forecast")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForecastSeedDTO {
    @NotNull
    @XmlElement(name = "day_of_week")
    private DayOfWeek dayOfWeek;
    @NotNull
    @Min(-20)
    @Max(60)
    @XmlElement(name = "max_temperature")
    private Double maxTemperature;
    @NotNull
    @Min(-50)
    @Max(40)
    @XmlElement(name = "min_temperature")
    private Double minTemperature;
    @NotNull
    @XmlElement(name = "sunrise")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private LocalTime sunrise;
    @NotNull
    @XmlElement(name = "sunset")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private LocalTime sunset;
    @XmlElement(name = "city")
    private Long cityId;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public LocalTime getSunrise() {
        return sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    public Long getCityId() {
        return cityId;
    }
}
