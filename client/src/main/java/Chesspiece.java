public class Chesspiece {
  String type;
  String color;

  public Chesspiece(String PieceType, String TeamColor) {
    this.type=PieceType;
    this.color=TeamColor;
  }

  public String getPieceType() {
    return type;
  }

  public String getTeamColor() {
    return color;
  }

  public String pieceMoves() {
    return null;
  }


}
