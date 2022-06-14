package si.lodrant.othello.gui;
import java.util.EnumMap;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.vodja.Vodja;
import si.lodrant.othello.vodja.VrstaIgralca;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartActionListener implements ActionListener{
    VrstaIgralca izbira_igralecCrni;
    VrstaIgralca izbira_igralecBeli;

    public StartActionListener(VrstaIgralca izbira_igralecCrni,  VrstaIgralca izbira_igralecBeli) {
        this.izbira_igralecCrni = izbira_igralecCrni;
        this.izbira_igralecBeli = izbira_igralecBeli;

    }

    public void actionPerformed(ActionEvent e) {
        Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
        Vodja.vrstaIgralca.put(Igralec.BLACK, izbira_igralecCrni); 
        Vodja.vrstaIgralca.put(Igralec.WHITE, izbira_igralecBeli); 
        Vodja.igramoNovoIgro();
        Vodja.okno.setVisible(true);

    }
    
}
