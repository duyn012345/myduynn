package myduynn.myduynn;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
public class QL_ThuChii {
    private int TT;
    private LocalDate Date;
    //private String Type;
    private int Price;
    private String Note;
    private String Type;//SimpleStringProperty
    private static int TTCounter = 0;

    public QL_ThuChii(int TT,LocalDate Date, String Type, int Price, String Note) {
        this.TT=TT;
        this.Date = Date;
        this.Type =Type;// new SimpleStringProperty(Type);
        this.Price = Price;
        this.Note = Note;
    }

    public int getTT() {
        return TT;
    }

    public void setTT(int TT) {
        this.TT = TT;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate Date) {
        this.Date = Date;
    }

    public String getType() {
        return Type;}//.get();


    public void setType(String Type) {
        this.Type=Type;//.set(Type);
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }
}
