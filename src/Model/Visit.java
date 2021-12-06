package Model;

import java.sql.Time;
import java.util.Date;

public class Visit{
    private int id;
    private int clientId;
    private int serviceId;
    private int employeeId;
    private Date date;
    private Time time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return id == visit.id && clientId == visit.clientId && serviceId == visit.serviceId && employeeId == visit.employeeId && date.equals(visit.date) && time.equals(visit.time);
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", serviceId=" + serviceId +
                ", employeeId=" + employeeId +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + clientId;
        result = 31 * result + serviceId;
        result = 31 * result + employeeId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}