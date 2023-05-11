# QuestPlugin v1.19.2
## 💬 Зависимости
Для работы плагина буду нужны другие плагины:
* [ProtocolLib 5.0.0](https://github.com/dmulloy2/ProtocolLib/releases/tag/5.0.0-rc1)
* [Vault 1.7.3](https://www.spigotmc.org/resources/vault.34315/)
* [EssentialsX](https://essentialsx.net/downloads.html)

Также вам нужно будет включить MySQL сервер и создать базу данных, после включение перейдите в файл config.yml и укажите все нужные значения:
```yml
database:
  host: "localhost" #Хост MySQL сервера
  port: "3306" #Порт MySQL сервера
  user: "root" #Пользователь
  password: "" #Пароль для пользователя
  data_base_name: "quest_plugin" #Имя базы данных
```
</br>

## 🔧 Настройки плагина (config.yml)
Опишу каждую категорию более-менее подробно. Одну из них я описал выше, она нужна для настройки MySQL сервера.

### messages
```yml
messages:
  did_the_part: "&fПрогресс квеста &b%quest_name%&f: &a%progress%&f/&3%full%" #Когда часть квеста будет выполнена
  did_the_quest: "&fКвест &a%quest_name% &fуспешно выполнен! Награда уже у вас" #Когда весь квест выполнен
```

### npc
```yml
npc:
  name: "_Degeron_" #Ник npc
  skin:
    texture: "ewogICJ0aW1lc3RhbXAiIDogMTY4MzcyODczNjYxOSwKICAicHJvZmlsZUlkIiA6ICIxMjdlM2RhMjU0YWQ0NTAwYWQ3ZjViOWNiNzgzMDlmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJfRGVnZXJvbl8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjYyZjk1ODIzOTUyMWU4MTg0NDczNDdmOTM4OWUwZmYwZmM4ODhkOGY2OTYwMjFiM2NmYjdkNmEzZjU3OWY1ZiIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0="
    signature: "J0vvytNUy/VnkDrlW6x1bMx/byQJkohGPPUWQLMw97WHp/jiKU5cFp61G3ieB8eqCr1g518J/KUxxHKnzF8JiEgO5JBS2hgIRlBm6tpDobS4sCdTkFnRvGMNDbDwWnmvugWDOyc2Q9S8ej7MejlmiflS59zI/RZxn1NRr6E4J4Es4/6QhC+B2oVA21NFkepvqrTL/V3jHgUO1JnWoh9EnoPwJsEo6jm2MncHUY7U3I80kA9pZPdagC1rr5azNoQeQPWAmPc+xbFYv66N/VFPV5BipaH0q9Tp/C+iokS4iYtsrc2qWCvmyi9NbclldwZRZFnu0XI84pckXafZgQc6ce7uRZOr4SEuTZ5yMAwj6t1r+d6nvTwp7B+DTMxN9gqNe18Ha3A4JdoRnh9qSnQdzS9RBSzCVy+1q9DdkSbDUuEY8nj0h47rKVVsB+lwZe+NGCPULzqjle53IC72sdglG4KwngaGigUvz0HFNNS48TFvDCqTaxzF6dY4UCRSKF3u55vWWXpZvGh6rJETjf+vO/j7tB1ncqvuGoj/N4nDQ/JKg6XetxQ04oKL6TCp4zCws8ZWcFMC0wqI1pH7jyBf3sEW+Vav4wp3q/k/Egbpt0orvfeiKdwbFKwSxsUmvbrJdRUB0BtCq+b664FuBbd6mBnoocSchmwHFEtdXk8ul6U="

  location: #Место, где он спавнится
    world: "world"
    x: -206
    y: 65
    z: -133
```
Чтобы установить скин, нужно указать 2 параметра. Их можно получиить через сайт https://mineskin.org/. Там есть все нужные инструкции, также можете погуглить как получить texture и signature у скина.

### menu
```yml
menu:
  name: "Квесты" #Название инвентаря для меню квестов
  size: 9 #Его размер
  minute_update: 45 #Минута часа, в которую квест обновляется
  decor: #Предметы декора, можете поменять на своё усмотрение, материалов может быть несколько
    LIGHT_GRAY_STAINED_GLASS_PANE: #Material предмета декора
      name: "&8by: _Degeron_" #Название предмета декора
      slots: #Где этот декор лежит
        - 1
        - 2
        - 3
        - 5
        - 6
        - 7

  buttons: #Не квест предметы
    stats_item: #Предмет со статистикой
      name: "&3Статистика"
      material: BOOK
      slot: 0
      lore: #Описанание предмета
        - "&aВыполнено: &2%completed%"
        - "&cОтменено: &4%canceled%"

    cancel_item: #Кнопка выхода из меню
      name: "&cЗакрыть меню"
      material: BARRIER
      slot: 8
      lore:

  quest_item: #Квестовый предмет
    slot: 4
    suggested: #Преложенный
      name: "&a[Квест] %name%"
      material: CHEST
      lore:
        - "&7Сложность: &6%difficulty%"
        - "&7Награда: &6%reward%"
        - ""
        - "&7%description%"
        - ""
        - "&bНажмите, чтобы начать выполнение"
        - "&eКвест обновится через &f%minutes% &eминут"

    selected: #Выбранный, вся информация загружается из файла quests.yml, здесь только название и описание
      name: "&a[Квест] %name%"
      lore:
        - "&7Сложность: &6%difficulty%"
        - "&7Награда: &6%reward%"
        - ""
        - "&7%description%"
        - ""
        - "&cНажмите, чтобы отменить выполнение"
        - "&fПрогресс: &a%progress%&f/&3%full%"

    done: #Если квест выполнен, но сервер его ещё не обновил
      name: "&aПредложенный квест выполнен"
      material: GOLD_INGOT
      lore:
        - "&eКвест обновится через &f%minutes% &eминут"

    cancel: #Если квест отменён, но сервер его ещё не обновил
      name: "&cПредложенный квест отменён"
      material: IRON_INGOT
      lore:
        - "&eКвест обновится через &f%minutes% &eминут"

    difficulty: #В quests.yml сложность указывается с помощью цифр, а здесь указывается что эти цифры значат.
      0: "Лёгкая"
      1: "Нормальная"
      2: "Сложная"
```
</br>

## 🐙 Настройки квестов (quests.yml)



