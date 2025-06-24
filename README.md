###目次
<p>1.学習しようとした背景</p>
<p>2.開発環境</p>
<p>3.クラス構成</p>
<p>4.ディレクトリ構成</p>

###学習の経緯
<p>前職のプロジェクトの機能を起こそうとしたが、バックエンドの幅を広げるため、静的言語のjavaのFWを用いて開発したかったので、ヘッダーにトークンを入れるバックエンドの開発を決意。
springSecurityをベースに外部サービスと連携できるようにAPIベースで作成</p>

###開発環境
| 言語・フレームワーク  | バージョン |
| --------------------- | ---------- |
| java                  | 17.0.7     |
| springboot            | 3.2.0      |
| Spring-Security       | 8.0        |
| java-jwt              | 4.2.2      |
| MySQL                 | 8.0        |

###クラス構成
<p>Account:SpringSecurityと連動する認証情報クラス</p>
<p>Task:ログインするだけだと寂しかったので、一般的なcrud操作ができるメモ用のクラス</p>
<pre>
###ファイル構成
~/{javaprojectのRoot}
├── /controller(フロントと連携する用のAPI)
│   ├──AccountCountroller.java
│   └──TaskController.java
│
├── /model(DB関連)
│   ├──/entity
│   │  ├──Account.java
│   │  └──Task.java
│   └──/repository
│      ├──AccountRepository.java
│      └──TaskRepository.java
│
└── /security(認証関連)
    ├── Securityconfig(初回ログインと次回以降ログインのフィルタ処理設定を管理)
    ├── /core(SpringSecurityのライブラリ拡張)
    │   ├──SecurityRecordExt.java(自作accountで認証できるようにライブラリに受け渡し)
    │   ├──SecuritySqlExt.java(認証済みの自作accountを検索できるようにオーバーライド)
    │   └──SecurityTokenExt.java(認証時の自作accountとライブラリのトークン機能の連携)
    ├── /filter
    │   ├──AccountRepository.java
    │   └──TaskRepository.java
    └── /payload
        └──LoginForm.java
</pre>
<p align="right">(<a href="#top">トップへ</a>)</p>

###参考記事
<br/>
https://qiita.com/shimori/items/5b74fc61bfd7cd57e5a7
<br/>
https://zenn.dev/jy8752/articles/1a00cc7b077a2e
