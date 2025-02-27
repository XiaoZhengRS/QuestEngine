package cn.inrhor.questengine.common.quest.target.bukkit

import cn.inrhor.questengine.api.target.TargetExtend
import cn.inrhor.questengine.api.target.util.TriggerUtils.itemTrigger
import cn.inrhor.questengine.api.target.util.TriggerUtils.triggerTarget
import cn.inrhor.questengine.common.quest.target.node.ObjectiveNode
import cn.inrhor.questengine.utlis.bukkit.ItemMatch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common5.Demand

object TCraftItem: TargetExtend<CraftItemEvent>() {

    override val name = "craft item"

    init {
        event = CraftItemEvent::class
        tasker{
            val p = whoClicked as Player
            val inv = p.inventory
            val item = inventory.result?: return@tasker p
            p.triggerTarget(name) { _, pass ->
                itemTrigger(pass, item, inv) && matrixItems(inventory, pass, inventory.matrix)
            }
        }
    }

    fun matrixItems(inventory: Inventory, pass: ObjectiveNode, matrix: Array<ItemStack>): Boolean {
        val content = pass.matrix
        if (content.isEmpty()) return true
        matrix.forEach {
            if (!itemsMatch(inventory, content, it)) return false
        }
        return true
    }

    fun itemsMatch(inventory: Inventory, s: List<String>, itemStack: ItemStack): Boolean {
        s.forEach {
            if (ItemMatch(Demand(it)).checkItem(itemStack, inventory)) return true
        }
        return false
    }

}