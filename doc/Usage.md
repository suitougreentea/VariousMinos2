## モード
* Endless: ゲームオーバーまで続くモード
* Contest: フィールド内のブロックをすべて消すとクリア
* Puzzle: 決められたピースで決められた条件を満たすとクリア
* Master: 実力が評価されるモード
* Survival: 指定レベルまで耐え切るとクリア
  * Easy: Lv300でクリア
  * FC(FreezeChallenge): ミノがすべてフリーズブロック製のもの、Lv1000でクリア

## ルール
* Classic: 単色ミノ、Nextは1つ、Hold無効、回転補正、出現補正無し、即落下無効
* ClassicPlus: Classicに複数のNext、Holdを追加したもの
* Standard: ClassicPlusに回転補正を追加し、即落下を有効にしたもの
* VariantClassic: もう一つのClassic
* VariantPlus: VariantClassicに複数のNext、Hold、更なる回転補正を追加したもの

## デフォルトのキー設定
* 左: 左キー
* 右: 右キー
* 上(即落下): 上キー
* 下(高速落下): 下キー
* A(左回転): Z
* B(右回転): X
* C(ホールド): C

キー設定は `save/player/00000000-0000-0000-0000-000000000000.json` を編集することで変更可能(http://minecraft.gamepedia.com/Key_Codes にキーコードの一覧表がある)

## エディターの操作方法
* 上下左右: 移動
* Enter: 決定、進む
* Esc: キャンセル、戻る
* F1: 保存
* フィールドエディター内
  * Space: ブロック追加
  * 左Shift: ブロック削除
  * WASD: ブロック選択
  * Q: スポイト
  * E: 行を追加
  * Shift+E: 行を削除
  * R: 行をコピー
  * 左Ctrl+上下左右: フィールドをずらす
* ステージリスト内(メニューのステージを選ぶところでEnter)
  * 上下でステージを選択
  * N: ステージを末尾に追加
  * I: ステージを挿入
  * C: ステージをコピー
  * D: ステージを削除
* (Contest) ミノエディター内
  * Space: ON/OFF切り替え
* (Puzzle) ミノエディター内
  * Z: 数値を-1
  * X: 数値を+1
  * N: ミノを末尾に追加
  * I: ミノを挿入
  * C: ミノをコピー
  * D: ミノを削除