package software.ulpgc.moneycalculator.swing;

import software.ulpgc.moneycalculator.Currency;
import software.ulpgc.moneycalculator.CurrencyDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingCurrencyDialog extends JPanel implements CurrencyDialog {

    private JComboBox<Currency> currencySelector;

    public SwingCurrencyDialog() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        currencySelector = new JComboBox<>();
        currencySelector.setFont(new Font("Arial", Font.PLAIN, 14));
        currencySelector.setForeground(Color.BLACK);
        currencySelector.setToolTipText("Select a currency");
        add(currencySelector);
    }

    @Override
    public CurrencyDialog define(List<Currency> currencies) {
        for (Currency currency : currencies) {
            currencySelector.addItem(currency);
        }
        return this;
    }

    private Component createCurrencySelector(List<Currency> currencies) {
        JComboBox<Currency> selector = new JComboBox<>();
        for (Currency currency : currencies) selector.addItem(currency);
        this.currencySelector = selector;
        return selector;
    }

    @Override
    public Currency get() {
        return currencySelector.getItemAt(currencySelector.getSelectedIndex());
    }
}
