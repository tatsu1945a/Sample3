package plugin.sample3;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    private int count;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        //getConfig().set("Message", "実行できなってよ");
        //getConfig().getString("Message");

        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("setLevel").setExecutor(new SetLevelCommand(this));
        getCommand("allSetLevel").setExecutor(new AllSetLevelCommand());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        Location playerLocation = player.getLocation();

        world.spawn(new Location(world, playerLocation.getX() + 3, playerLocation.getY(), playerLocation.getZ()), Chicken.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            //Player player = (Player) sender;
            if(args.length == 1) {
                player.setLevel(Integer.parseInt(args[0]));
            } else {
                player.sendMessage("引数を１つにして下さい");
            }

        }
        return false;
    }



    /**
     * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
     *
     * @param e イベント
     */
    // public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {

        // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
        Player player = e.getPlayer();
        World world = player.getWorld();

        // BigInteger型の val を定義
        //BigInteger val = new BigInteger(Integer.toString(count));
        // val が素数であるかの判定 isProbablePrimeメソッドを使用
        //if (val.isProbablePrime(1)) {
        //for (int i=0; i<4; i++) {
        //    System.out.println(val + " は素数です");


        List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.GREEN);
        if(count % 2 == 0){
            for(Color color : colorList) {

                // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
                Firework firework = world.spawn(player.getLocation(), Firework.class);

                // 花火オブジェクトが持つメタ情報を取得。
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                // メタ情報に対して設定を追加したり、値の上書きを行う。
                // 今回は青色で星型の花火を打ち上げる。
                fireworkMeta.addEffect(
                    FireworkEffect.builder()
                        .withColor(color)
                        .with(Type.BALL_LARGE)
                        .withFlicker()
                        .build());
                fireworkMeta.setPower(1);

                // 追加した情報で再設定する。
                firework.setFireworkMeta(fireworkMeta);
            }
        }
        count++;
        //System.out.println("今のカウント；" + count);


    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e){
        Player player = e.getPlayer();
        ItemStack[] itemStacks = player.getInventory().getContents();
        for(ItemStack item : itemStacks) {
            if(!Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64) {
                item.setAmount(64);
            }
        }

        player.getInventory().setContents(itemStacks);
    }
}
