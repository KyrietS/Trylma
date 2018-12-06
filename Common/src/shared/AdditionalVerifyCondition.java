package shared;

//klasa wspomagająca dla MovementStrategy, umożliwia dodawanie dodatkowych warunków weryfikacji ruchu
public interface AdditionalVerifyCondition
{
    boolean verify();
}
