# Reinforcement-Learning for Maze Solving
![Carrot](gfx/carrot.png)
<br />
Projekt ten stanowi implementację agenta uczenia ze wzmacnianiem, który zdobywa umiejętność rozwiązywania labiryntu przy użyciu algorytmu Q-learning. Dodatkowo, zawiera również generator labiryntów, który tworzy zróżnicowane trasy do eksploracji przez agenta.
<br />
Projekt zaliczeniowy zajęć laboratoryjnych przedmiotu "Fizyka komputerowa", na V semestrze Technologii Komputerowych, wydziału fizyki, Uniwersytetu im. Adama Mickiewicza w Poznaniu. Temat nr. 26 "Uczenie ze wzmacnianiem. Labirynt. (Przykład: https://strikingloo.github.io/reinforcement-learning-beginners) "
<br />
<br />
W dzisiejszym świecie, gdzie sztuczna inteligencja i automatyka stają się coraz bardziej obecne, zrozumienie i implementacja algorytmów uczenia ze wzmacnianiem staje się kluczowe. Projekt ten ma na celu nie tylko eksplorację i zrozumienie tych koncepcji, ale także praktyczne ich zastosowanie w rozwiązaniu konkretnego problemu, jakim jest przejście labiryntu.
Głównym celem projektu jest zaimplementowanie agenta zdolnego do samodzielnego rozwiązywania labiryntu przy użyciu algorytmu Q-learn. Ponadto, projekt obejmuje stworzenie generatora labiryntów, który pozwoli na tworzenie różnorodnych środowisk do testowania zdolności agenta.

## 🚀 Czym Jest Reinforcement Learning?

Reinforcement Learning, czyli uczenie ze wzmacnianiem, to koncepcja z dziedziny sztucznej inteligencji, gdzie agent uczony jest podejmować decyzje w dynamicznym środowisku. Kluczowym elementem jest to, że agent zdobywa doświadczenie, interakcjonując z otoczeniem, i otrzymuje informację zwrotną w postaci nagród lub kar.

### 👾 Kluczowe Elementy RL:

- **Agent:** To podmiot, który podejmuje decyzje w środowisku, starając się maksymalizować sumę nagród.

- **Środowisko:** To kontekst, w którym agent działa. Środowisko reaguje na decyzje agenta, dostarczając nowych stanów i nagród.

- **Akcje:** Są to decyzje podejmowane przez agenta w danym stanie środowiska.

- **Stany:** Reprezentują określony kontekst lub sytuację w środowisku.

- **Nagrody i kary:** Są używane do oceny decyzji agenta. Cel agenta to maksymalizacja łącznej zdyskontowanej sumy nagród.

- **Funkcja Wartości:** Ocenia, jak dobre są różne stany lub akcje w danym kontekście.

- **Eksploracja a Eksploatacja** Agent musi zbalansować eksplorację nowych działań i eksploatację już znanego, aby osiągnąć optymalne wyniki.

### 🔍 Dlaczego To Ważne?

Reinforcement Learning ma szerokie zastosowanie, od sterowania robotami po automatyczne podejmowanie decyzji w grach komputerowych. To narzędzie umożliwia agentom samodzielne doskonalenie strategii, poprzez interakcję z otoczeniem.

## 📎 Algorytm Q-learn i projekt agenta
W projekcie zaimplementowano algorytm Q-learn, który jest jednym z popularnych algorytmów uczenia ze wzmacnianiem. Algorytm ten polega na budowaniu funkcji wartości akcji, zwanej również funkcją Q, która określa, jak dobre są różne akcje w danym stanie środowiska. <br />
Na początku algorytmu tworzona jest tabela Q, w której przechowywane są wartości Q dla każdej kombinacji stanu i akcji. W mojej implementacji jest to dwuwymiarowa tabela states wypełniona instancjami klasy State. Ułatwia to pobieranie najlepszej akcji i wartości dla każdego stanu.
```java
private State[][] states;
```
```java
public class State
{
    public double [] qValues = {0,0,0,0};

    public int GetBestAction()
    {
        int bestAction = 0;
        double bestValue = -100;

        for(int i=0; i<qValues.length; i++)
        {
            if(qValues[i] > bestValue || (qValues[i] == bestValue && (Math.random() > 0.5f)))
            {
                bestAction=i;
                bestValue=qValues[i];
            }
        }

        return  bestAction;
    }
    public double GetBestValue()
    {
        double bestValue = -100;

        for (double qValue : qValues)
        {
            if (qValue > bestValue) bestValue = qValue;
        }

        return  bestValue;
    }
};
```
Każdy agent ma przypisywane te wartości w sposób losowy. Oscylują one wokół pewnych wartości uznanych za standardowe i przynoszące zadowalające rezultaty w każdym środowisku
```java
epsilon = 0.75 + Math.random() * (0.75 - 0.5);
a = 0.05 + Math.random() * (0.5 - 0.05);
y = 0.5 + Math.random() * (0.99 - 0.5 );
```
Agent, będąc w danym stanie, wybiera akcję do wykonania. Wybór ten może być zgodny z zasadą eksploatacji (wybieranie najlepszej znanej akcji) lub eksploracji (wybieranie losowej akcji w celu poszerzenia wiedzy agenta). Następnie agent wykonuje wybraną akcję, przechodząc do nowego stanu środowiska. Po wykonaniu akcji agent aktualizuje wartość Q zgodnie ze wzorem:

![Wzór Q-learning](gfx/Q-learning-equation.svg)

- **Q(s, a)** to wartość Q dla stanu \( s \) i akcji \( a \),
- **α** to współczynnik uczenia,
- **R** to nagroda za wykonanie akcji,
- **γ**  to współczynnik dyskontowania przyszłych nagród,
- **\( s' \) i \( a' \)** to kolejny stan i akcja.

### 👾Podstawą działania algorytmu są trzy zmienne:

- **Epsilon (ϵ) - Exploration Probability.** <br />
Kontroluje balans pomiędzy eksploracją a eksploatacją. Eksploracja polega na podejmowaniu losowych akcji w celu odkrywania nowych możliwości, podczas gdy eksploatacja polega na wybieraniu akcji, które agent uważa za obecnie najlepsze.<br />
Dla małych wartości ϵ, agent bardziej skupia się na eksploatacji, wybierając akcje na podstawie obecnej wiedzy. <br />
Dla dużych wartości  ϵ, agent częściej eksploruje, co może pomóc w odkryciu lepszych strategii.
- **Alfa (α) - Learning Rate** <br />
Określa, jak szybko agent aktualizuje swoją wiedzę (Q-values) w trakcie uczenia się. Wartość  α kontroluje, jak bardzo nowe informacje wpływają na już istniejące wartości Q.<br />
Dla małych wartości α, agent bardziej kieruje się wcześniejszymi doświadczeniami, co sprawia, że uczenie jest bardziej stabilne, ale wolniejsze. <br />Dla dużych wartości α, agent szybciej dostosowuje się do nowych informacji, ale może być bardziej podatny na szumy w danych.
- **Gamma (γ) - Discount Factor** <br />
Określa, jak bardzo agent zwraca uwagę na przyszłe nagrody. Wpływa na to, jak bardzo agent jest zorientowany na zdyskontowane nagrody w przyszłości w porównaniu z natychmiastowymi nagrodami.<br />
Dla małych wartości  γ, agent bardziej skupia się na natychmiastowych nagrodach, ignorując przyszłe korzyści. <br />
Dla dużych wartości γ, agent bardziej uwzględnia zdyskontowane nagrody, co sprawia, że jest bardziej długoterminowy.

Agent podejmuje decyzje w poniższej metodzie:
```java
public int chooseAction()
    {
        steps++;
        if (Math.random() < epsilon)
        {
            //best
            return states[posX][posY].GetBestAction();
        }
        else
        {
            //random
            return (int)(Math.random()*4);
        }
    }
```
Widać tutaj jak wartość ϵ bezpośrednio wpływa na czas jaki agent poświęca na zwiedzanie labiryntu, w stosunku do ślepego podążania za nagrodami.

![Hare1](gfx/BabyKicajec0.png)
![Hare2](gfx/BabyKicajec1.png)

## 📎 Generowanie Labiryntów

Projekt wykorzystuje algorytm "recursive backtracking" do generowania labiryntów w formie tablicy 2D. Algorytm używa rekurencji do eksplorowania labiryntu. Punkty są wybierane losowo, a następnie odwiedzane sąsiednie punkty, usuwając ściany między nimi. Jeśli dany punkt nie ma dostępnych sąsiadów, algorytm wraca do poprzedniego punktu (backtrack), kontynuując proces.

### 👾Kroki algorytmu:
- Wybierz punkt początkowy
- Wybierz losową sąsiednią komórkę. Utwórz przejście tylko wtedy, gdy komórka ta nie została jeszcze odwiedzona
- Powtarzaj proces, aż nie będzie już dostępnych sąsiednich komórek
- Rozpocznij cofanie się (backtrack) aż do momentu, gdy ponownie będzie możliwe wybranie komórki
- Algorytm kończy się, gdy wrócisz do punktu początkowego

Wybierz ścieżkę i idź nią, aż dotrzesz do ślepego zaułka. Następnie cofnij się do momentu, w którym możesz zacząć wybierać ścieżki ponownie. Powtarzaj to, aż dotrzesz do celu.

## 👩🏽‍💻🧑🏽‍💻 Autorzy

Kod: Miłosz Klim, Wydział Fizyki, Technologie Komputerowe semestr V<br />
Grafika: Wiktoria Bielecka, Wydział Fizyki, Technologie Komputerowe semestr V<br />
Źródła:
- https://aryanab.medium.com/maze-generation-recursive-backtracking-5981bc5cc766
- https://strikingloo.github.io/reinforcement-learning-beginners 
<br />

Grudzień 2023