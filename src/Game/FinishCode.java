package Game;

public enum FinishCode
{
    NONE,			//승부가 나지 않은 상태
    PLAYER,			//플레이어가 승리
    PLAYERBUSTED,	//플레이어 BUSTED
    DEALER,			//딜러가 승리
    DEALERBUSTED,	//딜러 BUSTED
    DRAW,			//비김
    NEEDCALC,		//누가 이겼는지 비교해야함
}