# Code Extend & Modify (other people's code)

## Question Specifications

請幫這個 project 添加兩個新功能:

1. 新增 Dependency 的 line (虛線箭頭)。
2. 點擊 port 時，與此 port 相連的線會被 highlight。
    - 和字面意思一樣，點擊 port 時線才會被 highlight，點擊線時不會觸發事件。
    - 點擊到沒有連接線的 port 時，也不會觸發事件。
    - 點擊其他地方，已 highlight 的線要取消 highlight。

若有其他 Bug 可以不予理會，也不需要改善程式架構，"專注在加入新功能" 即可。

## Observation

### Added Feature (according to spec)

1. add line: DependencyLine
    1. mode & type: new class DependencyLine in instance folder.
        1. modify the line: paintComponent() use while loop draw the sub-line into dotted line. (but still have a little BUG)
    2. add method:
        1. isDependencyLine() in class Core
    3. modify method:
        1. in class Core:
            1. add DependencyLine return 3 in isLine()
            2. add DependencyLine return 6 in isFuncComponent() (**by case swap change to 5**)
            3. isFuncComponent(): case swap (5->6 and 6->5)
        2. in class CanvasPanelHandler:
            1. add DependencyLine as case 6 in **both** ActionPerformed()
            2. add DependencyLine as case 3 in addLine()
            3. add DependencyLine as case 6 in setSelectAllType()
            4. **both** ActionPerformed(): case swap  (4->5, 5->6 and 6->4)
            5. setSelectAllType(): case swap  (5->6 and 6->5)
            6. selectByClick(): case swap (5->6)
            7. selectBySide(): case swap (6->5)
        3. in class FuncPanelHandler:
            1. add DependencyLine as case 6 in getFunc(), getIcon()
            2. getFunc(), getIcon(): case swap (4->5, 5->6 and 6->4)
        4. in class GroupContainer:
            1. add DependencyLine as case 6
            2. setSelect(): case swap (5->6 and 6->5)
2. port highlight: BUG fixed and feature apply.
    1. precondition click event (not pressed).
    2. when click event on an object (class or usecase), finding side then select all line which connect to this object with same side.
        1. selectBySide() in CanvasPanelHandler
        2. checkOnSide() in AssociationLine, CompositionLine and GeneralizationLine

### Addition Feature (not on the spec)

1. selection on line object: fixed the BUG in method paintSelect() for all file of Line object, the fp & tp were not deduction the base location by itself.
    1. addtional problem (draw): the selected show box is realy ugly, adjust the to box not to overlay the base object.
        1. still ugly
        2. method paintSelect() over AssociationLine and CompositionLine and GeneralizationLine)
    2. addtional problem (size): the number in class xxxLine method reSize(), should change to some related with the variable selectBoxSize.
2. Drag Algorithm: merge method groupSelect() groupInversSelect() to method groupSelectPLUS(), in order for the correct way on multi-selection check.
3. addLine to vector: fixed the BUG of non adding the line object to "members" vector in class CanvasPanelHandler, the origin only add this to "contextPanel" (aka. the canvas).
4. draw group object: draw rect for the group object.
5. draw line: fixed BUG on line in any group object (additional BUG fixed: outside the group object), since the base is incorrect.

### Found BUGs (no change)

1. usecase object click event: isInside() didnot consider the Oval situation.
2. line side: On new line, side check and set maybe wrong. clicked on A but set on non-A, but this can bypass the false line detection in method addLine() class CanvasPanelHandler.

## Comment

This work (Extend and modify other people code) took me about 6 hours to finish all feature that has to be done. Although, fixed some BUGs in the progress, there still were plenty in the code. Since I got a final exam in another lesson this semester 4 day later, therefore do not get any time and do not want to see this buggy code anymore.
