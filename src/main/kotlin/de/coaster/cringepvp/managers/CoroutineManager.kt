package de.coaster.cringepvp.managers

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedGameProfile
import de.coaster.cringepvp.CringePvP
import de.coaster.cringepvp.CringePvP.Companion.coroutineScope
import de.coaster.cringepvp.extensions.broadcastActionbar
import de.coaster.cringepvp.extensions.plus
import de.coaster.cringepvp.listeners.GamemodeListeners
import dev.fruxz.ascend.extension.data.randomInt
import dev.fruxz.stacked.text
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.math.min
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object CoroutineManager {

    init {
        println("CoroutineManager initialized")
        startClearLag()
        startBroadcast()
        startKristallMine()
        startIdleCash()
    }

    fun startCoroutine(coroutine: suspend () -> Unit) {
        coroutineScope.launch {
            coroutine()
        }
    }

    fun startCoroutine(coroutine: suspend () -> Unit, delay: Long) {
        coroutineScope.launch {
            delay(delay)
            coroutine()
        }
    }

    private fun startClearLag() {
        startCoroutine {
            while (true) {
                delay(90000)
                broadcastActionbar(text("<gold>[</gold><green>ClearLag</green><gold>]</gold> <aqua>Es werden alle am Boden liegenden Items entfernt in 30s</aqua>"))
                delay(15000)
                broadcastActionbar(text("<dark_purple>{</dark_purple><dark_red>NoLaggg</dark_red><dark_purple>}</dark_purple> <dark_green>Es werden alle Items in 15 Sekunden gelöscht.</dark_green>"))
                delay(12000)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » <gray>Löschung in 3..</gray>"))
                delay(1000)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » Löschung in 2.."))
                delay(800)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » <gray>Löschung in 1,2..</gray>"))
                delay(1200)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » <gray>Jetzt wird gelöscht!</gray>"))

                Bukkit.getScheduler().runTask(CringePvP.instance, kotlinx.coroutines.Runnable{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:item]")
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=arrow,nbt={inGround:1b}]")
                })
            }
        }
    }

    private fun startBroadcast() {
        startCoroutine {
            while (true) {
                delay(randomInt(60 .. 500) * 1000L)
                Bukkit.broadcast(text("<gold><b>CringePvP</b></gold> » Joine doch unserem <click:open_url:'https://discord.gg/KAypYTgxKH'><b><hover:show_text:'Klick mich'>discord</hover></b></click> <3"))
                delay(randomInt(60 .. 500) * 1000L)
                Bukkit.broadcast(text("<dark_aqua><b>CringePvP</b></dark_aqua> » Spielt immer fair und bleibt schön cringe."))
                delay(randomInt(60 .. 500) * 1000L)
                Bukkit.broadcast(text("<dark_purple><b>CringePvP</b></dark_purple> » Kauft euch jetzt den super coolen <b><gold><click:open_url:'https://store.cringepvp.de/checkout/packages/add/5247669/single'><hover:show_text:'Klicke mich'>LEGENDARY</hover></click></gold></b> Rang!"))
            }
        }
    }

    fun shootItems(location: Location, player: Player, items: Array<ItemStack>) {
        items.forEachIndexed { index: Int, item: ItemStack ->
            Bukkit.getScheduler().runTaskLater(CringePvP.instance, Runnable {
                val itemInWorld = player.world.dropItem(location, item)
                itemInWorld.velocity = location.toVector().subtract(player.location.toVector()).normalize().multiply(-0.2).add(Vector(0.0, 0.3, 0.0))
//                    .rotateAroundY(
//                        Math.toRadians(
//                            (Math.random() * 360)
//                        )
//                    )
                itemInWorld.pickupDelay = 20
                itemInWorld.setCanMobPickup(false)
                player.world.playSound(itemInWorld.location, Sound.ENTITY_CAT_HISS, 1.0f, 1.0f)
            }, 20 * index.toLong())
        }
    }

    private fun startKristallMine() {
        startCoroutine {
            while (true) {
                delay(1.minutes)
                GamemodeListeners.kristallFuellstand = min(GamemodeListeners.kristallFuellstand + 1, 10)
            }
        }
    }

    private fun startIdleCash() {
        startCoroutine {
            while (true) {
                delay(1.seconds)

                PlayerCache.all().forEach { player ->
                    PlayerCache.updateCringeUser(player.copy(onlineTime = player.onlineTime + 1.seconds, coins = player.coins + player.idleCash))
                }
            }
        }
    }


}