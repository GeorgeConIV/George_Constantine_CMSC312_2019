package commands;

import java.util.Optional;

public class Calculate implements Operation
{
    Integer cycleCount;
    Integer cyclesRemaining;
    Boolean hasCrit;
    Character critVar;

    public Calculate(Integer cycleCount, Boolean hasCrit, Optional<Character> critVar)
    {
        this.cycleCount = cycleCount;
        this.cyclesRemaining = cycleCount;
        this.hasCrit = hasCrit;
        if(critVar.isPresent())
            this.critVar = critVar.get();
        else
            this.critVar = '0';
    }

    public boolean critical()
    {
        return hasCrit;
    }

    public void Run()
    {
        cyclesRemaining--;
    }

    public Integer getCyclesRemaining()
    {
        return cyclesRemaining;
    }

    public boolean getIO()
    {
        return false;
    }

    public Boolean getHasCrit(){
        return hasCrit;
    }

    public Character getCritVar(){
        return critVar;
    }

    @Override
    public String toString()
    {
        if(!hasCrit)
            return ("CALCULATE ".concat(cyclesRemaining.toString()));
        else
            return ("CALCULATE ".concat(cyclesRemaining.toString()).concat(" CRIT VAR: ").concat(critVar.toString()));
    }


}
