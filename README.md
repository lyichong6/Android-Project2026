# Android-Project2026

移动平台技术开发课程 Android 作业项目（2026 春夏）。

当前已完成：

- 必做功能 #1
  - `MainActivity` 提供「添加专辑」「查看收藏」入口
  - `AddAlbumActivity` 通过 iTunes Search API 查询，列表展示匹配结果（专辑名、歌手、流派、发行年份），失败或无结果时有提示
- 必做功能 #2
  - 每条查询结果右侧可单独「添加」到收藏
  - `AlbumListActivity` 使用 `RecyclerView` 展示收藏列表
- 选做功能 #3（持久化）
  - 使用 `SharedPreferences` 保存收藏（专辑名、歌手、流派、发行年份）
  - 进程被杀或冷启动后，收藏列表会从本地恢复
- 选做功能 #4（专辑封面）
  - 查询结果列表与收藏列表均显示封面（网络图，使用 Coil）
  - 封面地址随收藏一并持久化，重启后仍可显示
